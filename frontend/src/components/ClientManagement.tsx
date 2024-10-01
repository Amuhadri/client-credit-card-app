import React, { useState, useEffect } from 'react';
import axios from 'axios';
import ClientTable from './ClientTable';
import SearchBar from './SearchBar';
import ClientForm from './ClientForm';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

interface Client {
    id: number;
    firstName: string;
    lastName: string;
    oib: string;
    cardStatus: string;
}

const ClientManagement: React.FC = () => {
    const [clients, setClients] = useState<Client[]>([]);
    const [currentClient, setCurrentClient] = useState<Client | null>(null);
    const [searchOib, setSearchOib] = useState('');

    useEffect(() => {
        const fetchClients = async () => {
            try {
                const response = await axios.get<Client[]>('http://localhost:8080/api/clients');
                setClients(response.data);
            } catch (error) {
                if (error instanceof Error) {
                    toast.error(`Error fetching clients: ${error.message}`, { position: 'top-right' });
                } else {
                    toast.error('An unknown error occurred while fetching clients.', { position: 'top-right' });
                }
            }
        };

        fetchClients();

        const socket = new WebSocket('ws://localhost:8080/card-status-updates');

        socket.onopen = () => {
            console.log('WebSocket connection opened');
        };

        socket.onmessage = (event) => {
            const updatedClient = JSON.parse(event.data);

            toast.info(`Status for oib: ${updatedClient.oib} has changed! New status is ${updatedClient.cardStatus}`, { position: 'top-right' });

            setClients((prevClients) =>
                prevClients.map((client) =>
                    client.oib === updatedClient.oib ? { ...client, cardStatus: updatedClient.cardStatus } : client
                )
            );
        };

        socket.onclose = () => {
            console.log('WebSocket connection closed');
        };

        return () => {
            socket.close();
        };
    }, []);

    const handleSearch = async () => {
        if (searchOib) {
            try {
                const response = await axios.get<Client>(`http://localhost:8080/api/clients/${searchOib}`);
                setClients([response.data]);
            } catch (error: unknown) {
                if (axios.isAxiosError(error)) {
                    if (error.response && error.response.status === 404) {
                        toast.error(`Client with OIB: ${searchOib} not found.`, { position: 'top-right' });
                        setClients([]);
                    } else {
                        toast.error(`Error fetching client: ${error.message}`, { position: 'top-right' });
                    }
                } else if (error instanceof Error) {
                    toast.error(`Unexpected error: ${error.message}`, { position: 'top-right' });
                } else {
                    toast.error('An unknown error occurred while fetching client.', { position: 'top-right' });
                }
            }
        } else {
            try {
                const response = await axios.get<Client[]>('http://localhost:8080/api/clients');
                setClients(response.data);
            } catch (error: unknown) {
                if (axios.isAxiosError(error)) {
                    toast.error(`Error fetching clients: ${error.message}`, { position: 'top-right' });
                } else if (error instanceof Error) {
                    toast.error(`Unexpected error: ${error.message}`, { position: 'top-right' });
                } else {
                    toast.error('An unknown error occurred while fetching clients.', { position: 'top-right' });
                }
            }
        }
    };



    const handleSaveClient = async (client: Client): Promise<boolean> => {
        try {
            let updatedClients;

            if (currentClient) {
                await axios.put(`http://localhost:8080/api/clients/${client.oib}`, client);
                updatedClients = clients.map((c) =>
                    c.oib === client.oib ? client : c
                );
            } else {
                await axios.post('http://localhost:8080/api/clients', client);
                updatedClients = [...clients, client];
            }

            setClients(updatedClients);
            setCurrentClient(null);

            return true;
        } catch (error) {
            if (error instanceof Error) {
                toast.error(`Greška prilikom spremanja klijenta: ${error.message}`, { position: 'top-right' });
            } else {
                toast.error('Došlo je do nepoznate greške prilikom spremanja klijenta.', { position: 'top-right' });
            }
            return false;
        }
    };


    const handleSendClientData = async (oib: string) => {
        try {
            const client = clients.find(c => c.oib === oib);
            if (!client) {
                toast.error(`Client with OIB: ${oib} not found`, { position: 'top-right' });
                return;
            }

            const newCardRequest = {
                firstName: client.firstName,
                lastName: client.lastName,
                oib: client.oib,
                status: client.cardStatus,
            };

            const response = await axios.post('http://localhost:8081/api/v1/card-request', newCardRequest);
            toast.success(`${response.data}`, { position: 'top-right' });

            const updatedClientStatus = {
                ...client,
                cardStatus: 'SENT',
            };

            await axios.put(`http://localhost:8080/api/clients/${oib}`, updatedClientStatus);

            const updatedClientResponse = await axios.get<Client>(`http://localhost:8080/api/clients/${oib}`);

            const updatedClient = updatedClientResponse.data;

            setClients(prevClients =>
                prevClients.map(c =>
                    c.oib === oib ? updatedClient : c
                )
            );

            toast.success(`Status za klijenta s OIB-om: ${oib} uspješno ažuriran na "SENT"`, { position: 'top-right' });

        } catch (error) {
            if (error instanceof Error) {
                toast.error(`Error sending client data: ${error.message}`, { position: 'top-right' });
            } else {
                toast.error('Došlo je do nepoznate greške prilikom slanja podataka klijenta.', { position: 'top-right' });
            }
        }
    };




    const handleEditClient = (client: Client) => {
        setCurrentClient(client);
    };

    const handleCancelEdit = () => {
        setCurrentClient(null);
    };

    const handleDeleteClient = async (oib: string): Promise<boolean> => {
        try {
            await axios.delete(`http://localhost:8080/api/clients/${oib}`);
            setClients(prevClients => prevClients.filter(client => client.oib !== oib));
            return true;
        } catch (error) {
            if (error instanceof Error) {
                toast.error(`Error deleting client: ${error.message}`, { position: 'top-right' });
            } else {
                toast.error('An unknown error occurred while deleting client.', { position: 'top-right' });
            }
            return false;
        }
    };

    return (
        <div>
            <ToastContainer
                position="top-right"
                autoClose={5000}
                hideProgressBar={false}
                newestOnTop={false}
                closeOnClick
                rtl={false}
                pauseOnFocusLoss
                draggable
                pauseOnHover
            />

            <ClientForm
                currentClient={currentClient}
                onSaveClient={handleSaveClient}
                onCancel={handleCancelEdit}
            />

            <SearchBar
                searchValue={searchOib}
                onSearchChange={(e) => setSearchOib(e.target.value)}
                onSearchClick={handleSearch}
            />

            {clients.length > 0 ? (
                <ClientTable
                    clients={clients}
                    onEdit={handleEditClient}
                    onDelete={handleDeleteClient}
                    onSendClientData={handleSendClientData}
                />
            ) : (
                <div>
                    <p>No clients found.</p>
                </div>
            )}
        </div>
    );
};


export default ClientManagement;
