import React from 'react';
import { toast } from 'react-toastify'; // Import react-toastify
import 'react-toastify/dist/ReactToastify.css'; // Import CSS for toast

interface Client {
    id: number;
    firstName: string;
    lastName: string;
    oib: string;
    cardStatus: string;
}

interface ClientTableProps {
    clients: Client[];
    onEdit: (client: Client) => void;
    onDelete: (oib: string) => Promise<boolean>; // onDelete now returns Promise<boolean> for success/failure
    onSendClientData: (oib: string) => void; // Add this new prop for sending data
}

const ClientTable: React.FC<ClientTableProps> = ({ clients, onEdit, onDelete, onSendClientData }) => {
    const handleDeleteClick = async (oib: string) => {
        if (window.confirm('Are you sure you want to delete this client?')) {
            const success = await onDelete(oib);
            if (success) {
                toast.success('Client successfully deleted!', { position: 'top-right' });
            } else {
                toast.error('An error occurred while deleting the client.', { position: 'top-right' });
            }
        }
    };

    return (
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>OIB</th>
                <th>Card Status</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            {clients.map((client) => (
                <tr key={client.oib}>
                    <td>{client.id}</td>
                    <td>{client.firstName}</td>
                    <td>{client.lastName}</td>
                    <td>{client.oib}</td>
                    <td>{client.cardStatus}</td>
                    <td>
                        <button onClick={() => onEdit(client)} style={{ marginLeft: '10px' }}>Edit</button>
                        <button onClick={() => handleDeleteClick(client.oib)} style={{ marginLeft: '10px' }}>Delete</button>
                        <button onClick={() => onSendClientData(client.oib)} style={{ marginLeft: '10px' }}>
                            Send
                        </button> {/* New Send Button */}
                    </td>
                </tr>
            ))}
            </tbody>
        </table>
    );
};

export default ClientTable;
