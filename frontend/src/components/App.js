// src/components/App.js
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import AppContent from './AppContent';
import ViewerContent from './viewer/ViewerContent'; 
import AdminContent from './admin/AdminContent';
import ViewerLogin from './viewer/ViewerLogin';
import EditorLogin from './editor/EditorLogin';
import EditorContent from './editor/EditorContent'; 
import CreateSession from './editor/CreateSession';
import JoinSession from './editor/JoinSession';
import SelectExistingProject from './editor/SelectExistingProject'; 
import Collaborative from './editor/CollaborativeRoom'; 
import CollaborativeCient from './editor/CollaborativeUser'; 
import SelectProject from './viewer/SelectProject'; 
import ViewProject from './viewer/ViewProject.js'; 
import AdminLogin from './admin/AdminLogin.js';

const App = () => {

  return (
    <Router>
      <div className="App">
        <main className="App-content">
          <Routes>
          <Route path="/home" element={<AppContent />} />
             <Route path="/admin" element={<AdminContent />} />
             <Route path="/adminLogin" element={<AdminLogin />} />
            <Route path="/viewerLogin" element={<ViewerLogin />} />
            <Route path="/ViewerContent" element={<ViewerContent />} /> 
            <Route path="/editorLogin" element={<EditorLogin />} />
            <Route path="/EditorContent" element={<EditorContent />} /> 
            <Route path="/create-session" element={<CreateSession />} />
            <Route path="/join-session" element={<JoinSession />} /> 
            <Route path="/select-existing-project" element={<SelectExistingProject />} /> 
            <Route path="/collaboratice-room-creator" element={<Collaborative />} /> 
            <Route path="/collaboratice-room" element={<CollaborativeCient />} /> 
            <Route path="/SelectProject" element={<SelectProject />} /> 
            <Route path="/ViewProject" element={<ViewProject />} /> 
          </Routes>
        </main>
      </div>
    </Router>
  );
};

export default App;
