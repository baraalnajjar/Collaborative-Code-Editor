// src/components/UsersList.js
import React from 'react';
import '../../CSS/UsersList.css'; 


const UsersList = ({ users, onBack }) => (
  <div className="viewers-list">
    <h2>Users List</h2>
    {users.length === 0 ? (
      <p>No users found.</p>
    ) : (
      <table className="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>First Name</th>
            <th>Last Name</th>
          </tr>
        </thead>
        <tbody>
          {users.map((user) => (
            <tr key={user.id}>
              <td>{user.id}</td>
              <td>{user.firstName}</td>
              <td>{user.lastName}</td>
            </tr>
          ))}
        </tbody>
      </table>
    )}
    <button onClick={onBack}>Back to Admin Options</button>
  </div>
);

export default UsersList;
