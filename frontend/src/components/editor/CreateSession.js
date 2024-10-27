import React, { useState, useEffect } from 'react';
import { request, setAuthHeader } from '../../helpers/axios_helper'; 
import { useNavigate } from 'react-router-dom';
import '../../CSS/CreateSession.css';

const CreateSession = () => {
    const [projectType, setProjectType] = useState('');
    const [click, setclicke] = useState('');
    const [languages, setLanguages] = useState([]); 
    const [loading, setLoading] = useState(false); 
    const [projectName, setProjectName] = useState(''); 
    const [selectedLanguage, setSelectedLanguage] = useState('');
    const navigate = useNavigate(); 
    const username = localStorage.getItem('username');

    useEffect(() => {
        if (projectType) {
            fetchLanguages();
        }
    }, [projectType]);

    const fetchLanguages = async () => {
        const token = localStorage.getItem('authToken');
        if (!token) {
            alert('Session expired. Please log in again.');
            return;
        }

        setAuthHeader(token);
        setLoading(true);

        try {
            console.log("Fetching languages...");
            const response = await request('GET', '/project/languages');
            console.log("Response received:", response);

            if (response && response.data && typeof response.data === 'object') {
                const languagesList = Object.values(response.data); 
                setLanguages(languagesList);
                console.log("Languages set:", languagesList);
            } else {
                console.error('Unexpected response format:', response);
                setLanguages([]);
            }
        } catch (error) {
            console.error('Error fetching languages:', error);
            setLanguages([]);
        } finally {
            setLoading(false);
        }
    };
    const handleLanguageSelection = (language) => {
        console.log(`Selected language: ${language}`);
        
        if (projectType === 'New') {
            handleSubmit();
        } else if (projectType === 'Existing') {
            navigate('/select-existing-project', { state: { language } }); 
        }
    };
    const handleSubmit = async (event) => {
        event.preventDefault();
        const token = localStorage.getItem('authToken');
        if (!token) {
            alert('Session expired. Please log in again.');
            return;
        }
        const requestBody = {
            language: selectedLanguage, 
            username: username
        };
        try {
            const response = await request('POST', '/project/create?projectName=' + encodeURIComponent(projectName), requestBody);
            console.log("Response received:", response);
    

            console.log('Project created successfully, check existing projects');
            alert('Project created successfully, check existing projects'); 
    
        } catch (error) {
            console.error('Error creating project:', error);
            alert('Error creating project: ' + error.message); 
        }
    };
    

    const colors = [
        'green-button',
        'blue-button',
        'red-button',
        'yellow-button',
        'purple-button',
        'orange-button', 
        'teal-button',    
        'pink-button',    
        'brown-button',
        'gray-button'     
    ];

    return (
        <div className="create-session-container">
            <h2>Create New Session</h2>
            <div className="project-selection">
                <button className="project-button" onClick={() => {setProjectType('New');setclicke('Create a project')  }}>New Project</button>
                <button className="project-button" onClick={() => {setProjectType('Existing');setclicke('Find a Project')  }}>Existing Project</button>
            </div>

            {projectType && (
                <div className="selected-project">
                    {loading ? (
                        <p>Loading languages...</p>
                    ) : languages.length > 0 ? (
                        <div className="language-list">
                            <h4> {click} </h4>
                            <div className="language-buttons">
                                {languages.map((language, index) => (
                                    <button 
                                        key={index} 
                                        className={`language-button ${colors[index % colors.length]}`} 
                                        onClick={() =>{
                                            localStorage.setItem('language', language); 
                                            setSelectedLanguage(language); 
                                            handleLanguageSelection(language);}}
                                    >
                                        {language}
                                    </button>
                                ))}
                            </div>
                        </div>
                    ) : (
                        <p>No languages available.</p>
                    )}
                </div>
            )}

{selectedLanguage && (
    <div className="project-name-container">
        <form onSubmit={handleSubmit}>
            <div className="project-name-input">
                <span className="input-label">Enter Project Name:</span>
                <input
                    type="text"
                    value={projectName}
                    onChange={(e) => setProjectName(e.target.value)}
                    required
                    className="project-name-input-field"
                />
            </div>
            
            <button 
            type="submit" className="create-project-button">
            Create {selectedLanguage} Project
            </button>
        </form>
    </div>
)}

        </div>
    );
};

export default CreateSession;
