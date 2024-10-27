// src/components/viewer/ViewerContent.js
import React, { useState, useEffect } from 'react';
import { request, setAuthHeader } from '../../helpers/axios_helper'; 
import { useNavigate } from 'react-router-dom';
import '../../CSS/ViewerContent.css'


const ViewerContent = () => {
    const [languages, setLanguages] = useState([]); 
    const [loading, setLoading] = useState(false); 
    const [selectedLanguage, setSelectedLanguage] = useState('');
    const navigate = useNavigate(); 

    useEffect(() => {
        fetchLanguages();
    }, []);

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
        setSelectedLanguage(language);
        localStorage.setItem('language', language); 
        localStorage.setItem('selectedLanguage', selectedLanguage);
        navigate('/SelectProject'); 
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
            <h2>Choose a Language</h2>
            <div className="selected-project">
                {loading ? (
                    <p>Loading languages...</p>
                ) : languages.length > 0 ? (
                    <div className="language-list">
                        <div className="language-buttons">
                            {languages.map((language, index) => (
                                <button 
                                    key={index} 
                                    className={`language-button ${colors[index % colors.length]}`} 
                                    onClick={() => handleLanguageSelection(language)}
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
        </div>
    );
};

export default ViewerContent;
