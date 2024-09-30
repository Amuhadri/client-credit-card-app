import React from 'react';

interface SearchBarProps {
    searchValue: string;
    onSearchChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
    onSearchClick: () => void;
}

const SearchBar: React.FC<SearchBarProps> = ({ searchValue, onSearchChange, onSearchClick }) => {
    return (
        <div style={{ display: 'flex', justifyContent: 'center', marginBottom: '20px' }}>
            <input
                type="text"
                value={searchValue}
                onChange={onSearchChange}
                placeholder="Enter OIB to search"
                className="input"
                style={{ marginRight: '10px', padding: '8px', width: '250px' }} // Dodatni stilovi
            />
            <button
                onClick={onSearchClick}
                className="button"
                style={{ padding: '8px 16px' }}
            >
                Search
            </button>
        </div>
    );
};

export default SearchBar;
