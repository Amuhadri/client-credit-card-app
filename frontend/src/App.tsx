import React, { useState } from 'react';
import './App.css';
import ClientManagement from "./components/ClientManagement";

const App: React.FC = () => {
    return (
        <div className="App">
            <h1>Welcome to Client Card Management System</h1>
            <ClientManagement />
        </div>
    );
};

export default App;
