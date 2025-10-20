import { useState } from 'react';
import api from '../services/api';
import './AddCurrency.css';

function AddCurrency({ onCurrencyAdded }) {
  const [currencyCode, setCurrencyCode] = useState('');
  const [username, setUsername] = useState('admin');
  const [password, setPassword] = useState('admin123');
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState({ type: '', text: '' });
  const [showAuth, setShowAuth] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!currencyCode.trim()) {
      setMessage({ type: 'error', text: 'Currency code cannot be empty' });
      return;
    }

    setLoading(true);
    setMessage({ type: '', text: '' });

    try {
      await api.addCurrency(currencyCode.toUpperCase(), username, password);
      setMessage({ type: 'success', text: `Currency ${currencyCode.toUpperCase()} added successfully!` });
      setCurrencyCode('');
      
      // Notify parent to refresh currency list
      if (onCurrencyAdded) {
        setTimeout(() => onCurrencyAdded(), 500);
      }
    } catch (err) {
      setMessage({ type: 'error', text: err.message });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="add-currency">
      <h2>Add New Currency (ADMIN)</h2>
      
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="currencyCode">Currency Code:</label>
          <input
            type="text"
            id="currencyCode"
            value={currencyCode}
            onChange={(e) => setCurrencyCode(e.target.value)}
            placeholder="e.g., USD, EUR, GBP"
            maxLength="3"
            disabled={loading}
          />
        </div>

        <button 
          type="button" 
          className="auth-toggle"
          onClick={() => setShowAuth(!showAuth)}
        >
          {showAuth ? '🔒 Hide' : '🔓 Show'} Admin Credentials
        </button>

        {showAuth && (
          <div className="auth-section">
            <div className="form-group">
              <label htmlFor="username">Username:</label>
              <input
                type="text"
                id="username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                disabled={loading}
              />
            </div>
            <div className="form-group">
              <label htmlFor="password">Password:</label>
              <input
                type="password"
                id="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                disabled={loading}
              />
            </div>
          </div>
        )}

        <button type="submit" className="submit-button" disabled={loading}>
          {loading ? 'Adding...' : 'Add Currency'}
        </button>
      </form>

      {message.text && (
        <div className={`message ${message.type}`}>
          {message.text}
        </div>
      )}
    </div>
  );
}

export default AddCurrency;
