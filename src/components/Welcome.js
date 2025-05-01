import React from 'react';
import { Link } from 'react-router-dom';
import './Welcome.css';

const Welcome = () => {
  return (
    <div className="welcome-container">
      <h1>Welcome to PopX</h1>
      <p className="welcome-text">
        Lorem ipsum dolor sit amet,<br />
        consectetur adipiscing elit.
      </p>
      <div className="button-group">
        <Link to="/create-account" className="btn btn-primary">
          Create Account
        </Link>
        <Link to="/login" className="btn btn-secondary">
          Already Registered? Login
        </Link>
      </div>
    </div>
  );
};

export default Welcome; 