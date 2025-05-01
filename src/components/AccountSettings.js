import React from 'react';
import './AccountSettings.css';

const AccountSettings = () => {
  return (
    <div className="account-settings-container">
      <h1>Account Settings</h1>

      <div className="user-info">
        <h2>Marry Doe</h2>
        <p>Marry@Gmail.Com</p>
      </div>

      <div className="account-description">
        <p>
          Lorem Ipsum Dolor Sit Amet, Consetetur Sadipscing<br />
          Elitr, Sed Diam Nonumy Eirmod Tempor Invidunt Ut<br />
          Labore Et Dolore Magna Aliquyam Erat, Sed Diam
        </p>
      </div>
    </div>
  );
};

export default AccountSettings; 