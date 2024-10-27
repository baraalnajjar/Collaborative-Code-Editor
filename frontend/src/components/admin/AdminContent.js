// src/components/AdminContent.js
import React, { Component } from 'react';
import AdminOptions from './AdminOptions';
import UserManagement from './UserManagement';
import UsersList from './UsersList';
import RegisterPage from './RegisterPage';
import AppContent from '../AppContent'
import { request, setAuthHeader } from '../../helpers/axios_helper';

class AdminContent extends Component {
  constructor(props) {
    super(props);
    this.state = {
      componentToShow: 'adminOptions',
      loading: false,
      users: [],
    };
  }

  onRegisterEditor = (e, firstName, lastName, username, password) => {
    e.preventDefault();
    this.registerUser(e, '/admin/registerEditor', firstName, lastName, username, password);
  };

  onRegisterViewer = (e, firstName, lastName, username, password) => {
    e.preventDefault();
    this.registerUser(e, '/admin/registerViewer', firstName, lastName, username, password);
  };

  registerUser = (e, url, firstName, lastName, username, password) => {
    const token = localStorage.getItem('authToken');
    if (!token) {
      alert('Session expired. Please log in again.');
      this.setState({ componentToShow: 'login' });
      return;
    }
    setAuthHeader(token);
    this.setState({ loading: true });

    request('POST', url, { firstName, lastName, login: username, password: password })
      .then(() => this.setState({ componentToShow: 'adminOptions', loading: false }))
      .catch(() => this.setState({ loading: false }));
  };

  handleRemoveEditor = () => {
    const editorId = prompt('Enter the editor ID to remove:');
    if (editorId) {
      this.setState({ loading: true });
      request('POST', `/admin/removeEditor/${editorId}`)
        .then(() => this.setState({ loading: false }))
        .catch(() => this.setState({ loading: false }));
    }
  };

  handleRemoveViewer = () => {
    const viewerId = prompt('Enter the viewer ID to remove:');
    if (viewerId) {
      this.setState({ loading: true });
      request('POST', `/admin/removeViewer/${viewerId}`)
        .then(() => this.setState({ loading: false }))
        .catch(() => this.setState({ loading: false }));
    }
  };

  handleViewEditors = () => {
    this.setState({ loading: true });
    request('GET', '/admin/viewEditor')
      .then((response) => this.setState({ editors: response.data, componentToShow: 'viewEditors', loading: false }))
      .catch(() => this.setState({ loading: false }));
  };

  handleViewViewers = () => {
    this.setState({ loading: true });
    request('GET', '/admin/viewViewer')
      .then((response) => this.setState({ viewers: response.data, componentToShow: 'viewViewers', loading: false }))
      .catch(() => this.setState({ loading: false }));
  };
  handleBackToHome = () => {
    window.location.href = 'http://localhost:3000/home';
};
  render() {
    const { componentToShow, loading, editors, viewers } = this.state;

    if (loading) {
      return <p>Loading...</p>;
    }

    switch (componentToShow) {
        case 'adminOptions':
        return (
          <AdminOptions
            onManageEditors={() => this.setState({ componentToShow: 'editorManagement' })}
            onManageViewers={() => this.setState({ componentToShow: 'viewerManagement' })}
            onLogout={() => {
              window.location.href = 'http://localhost:3000/home';
            }}
          />
        );
      case 'editorManagement':
        return (
          <UserManagement
          onAddViewer={() => this.setState({ componentToShow: 'registerEditor' })}
          onRemoveViewer={this.handleRemoveEditor}
            onViewViewers={this.handleViewEditors}
            onBack={() => this.setState({ componentToShow: 'adminOptions' })}
            
          />
        );
      case 'viewerManagement':
        return (
          <UserManagement
            onAddViewer={() => this.setState({ componentToShow: 'registerViewer' })}
            onRemoveViewer={this.handleRemoveViewer}
            onViewViewers={this.handleViewViewers}
            onBack={() => this.setState({ componentToShow: 'adminOptions' })}
          />
        );
      case 'viewEditors':
        return <UsersList users={editors} onBack={() => this.setState({ componentToShow: 'adminOptions' })} />;
      case 'viewViewers':
        return <UsersList users={viewers} onBack={() => this.setState({ componentToShow: 'adminOptions' })} />;
      case 'registerEditor':
        return (
          <RegisterPage 
            role="Editor" 
            onRegister={this.onRegisterEditor} 
            onBack={() => this.setState({ componentToShow: 'adminOptions' })} 
          />
        );
      case 'registerViewer':
        return (
          <RegisterPage 
            role="Viewer" 
            onRegister={this.onRegisterViewer} 
            onBack={() => this.setState({ componentToShow: 'adminOptions' })} 
          />
        );
        case 'appContent':
        return <AppContent />;
      default:
        return <p>Invalid component state.</p>;
    }
  }
}

export default AdminContent;
