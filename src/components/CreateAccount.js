import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import './CreateAccount.css';

const CreateAccount = () => {
  const [formData, setFormData] = useState({
    fullName: 'Marry Doe',
    phoneNumber: 'Marry Doe',
    email: 'Marry Doe',
    password: 'Marry Doe',
    companyName: 'Marry Doe',
    isAgency: false
  });

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Handle account creation logic here
    console.log(formData);
  };

  return (
    <div className="create-account-container">
      <h1>Create your<br />PopX account</h1>

      <form onSubmit={handleSubmit} className="create-account-form">
        <div className="form-group">
          <label>Full Name*</label>
          <input
            type="text"
            name="fullName"
            value={formData.fullName}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label>Phone number*</label>
          <input
            type="tel"
            name="phoneNumber"
            value={formData.phoneNumber}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label>Email address*</label>
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label>Password*</label>
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label>Company name</label>
          <input
            type="text"
            name="companyName"
            value={formData.companyName}
            onChange={handleChange}
          />
        </div>

        <div className="form-group radio-group">
          <label>Are you an Agency?*</label>
          <div className="radio-options">
            <label>
              <input
                type="radio"
                name="isAgency"
                checked={formData.isAgency}
                onChange={() => setFormData(prev => ({ ...prev, isAgency: true }))}
              />
              Yes
            </label>
            <label>
              <input
                type="radio"
                name="isAgency"
                checked={!formData.isAgency}
                onChange={() => setFormData(prev => ({ ...prev, isAgency: false }))}
              />
              No
            </label>
          </div>
        </div>

        <button type="submit" className="btn btn-primary">
          Create Account
        </button>
      </form>
    </div>
  );
};

export default CreateAccount; 