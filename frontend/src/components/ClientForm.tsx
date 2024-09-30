import React, { useState, useEffect } from 'react';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

interface Client {
    id: number;
    firstName: string;
    lastName: string;
    oib: string;
    cardStatus: string;
}

interface ClientFormProps {
    currentClient: Client | null;
    onSaveClient: (client: Client) => Promise<boolean>;
    onCancel: () => void;
}

const ClientForm: React.FC<ClientFormProps> = ({ currentClient, onSaveClient, onCancel }) => {
    const [formClient, setFormClient] = useState<Client>({
        id: 0,
        firstName: '',
        lastName: '',
        oib: '',
        cardStatus: ''
    });

    useEffect(() => {
        if (currentClient) {
            setFormClient(currentClient);
        } else {
            setFormClient({
                id: 0,
                firstName: '',
                lastName: '',
                oib: '',
                cardStatus: ''
            });
        }
    }, [currentClient]);

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormClient((prevClient) => ({ ...prevClient, [name]: value }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        try {
            const success = await onSaveClient(formClient);
            if (success) {
                if (currentClient) {
                    toast.success('Client successfully updated!', { position: 'top-right' });
                } else {
                    toast.success('New client successfully added!', { position: 'top-right' });
                }
                if (!currentClient) {
                    setFormClient({
                        id: 0,
                        firstName: '',
                        lastName: '',
                        oib: '',
                        cardStatus: ''
                    });
                }
            } else {
                toast.error('Error saving client.', { position: 'top-right' });
            }
        } catch (error) {
            toast.error('An error occurred while saving the client.', { position: 'top-right' });
        }
    };

    return (
        <div>
            <h2>{currentClient ? 'Edit User' : 'New User'}</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>First Name:</label>
                    <input
                        type="text"
                        name="firstName"
                        value={formClient.firstName}
                        onChange={handleInputChange}
                        required
                    />
                </div>
                <div>
                    <label>Last Name:</label>
                    <input
                        type="text"
                        name="lastName"
                        value={formClient.lastName}
                        onChange={handleInputChange}
                        required
                    />
                </div>
                <div>
                    <label>OIB:</label>
                    <input
                        type="text"
                        name="oib"
                        value={formClient.oib}
                        onChange={handleInputChange}
                        required
                        disabled={!!currentClient}
                    />
                </div>
                <div>
                    <label>Card Status:</label>
                    <input
                        type="text"
                        name="cardStatus"
                        value={formClient.cardStatus}
                        onChange={handleInputChange}
                        required
                    />
                </div>
                <div>
                    <button type="submit">{currentClient ? 'Update' : 'Add'} Client</button>
                    {currentClient && (
                        <>
                            <button
                                type="button"
                                onClick={onCancel}
                                style={{ marginLeft: '10px' }}
                            >
                                Cancel
                            </button>
                        </>
                    )}
                </div>
            </form>
        </div>
    );
};

export default ClientForm;
