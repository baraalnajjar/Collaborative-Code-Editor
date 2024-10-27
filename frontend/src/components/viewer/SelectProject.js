import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { request, setAuthHeader } from '../../helpers/axios_helper';
import '../../CSS/SelectExistingProject.css';

const SelectProject = () => {
    const navigate = useNavigate();
    const [projects, setProjects] = useState([]); 
    const [loading, setLoading] = useState(false); 
    const [error, setError] = useState(null);
    const language = localStorage.getItem('language');

    useEffect(() => {
        if (language) {
            fetchProjects();
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [language]);

    const fetchProjects = async () => {
        setLoading(true); 
        const token = localStorage.getItem('authToken');
        if (!token) {
            alert('Session expired. Please log in again.');
            return;
        }

        setAuthHeader(token); 

        try {
            console.log("Fetching projects for language:", language);
            const response = await request('GET', `/project?language=${language}`); 
            console.log("Response received:", response); 
            setProjects(response.data.projects || []); 
        } catch (error) {
            console.error('Error fetching projects:', error);
            setError('Failed to fetch projects.'); 
        } finally {
            setLoading(false); 
        }
    };

    const handleProjectSelection = (project) => {
        localStorage.setItem('language', language);
        localStorage.setItem('project', project);
        console.log(`Selected project: ${project}`);
        navigate('/ViewProject'); 

    };

    return (
        <div className="container"> 
            <h2>Select a Project</h2>
            {loading && <p>Loading projects...</p>}
            {error && <p>{error}</p>}
            {language ? (
                <>
                    <p>{language} Projects:</p>
                    {projects.length > 0 ? (
    <ul className="project-list">
        {projects.map((project, index) => (
            <li key={index}> 
                <button
                    onClick={() => handleProjectSelection(project)} 
                    className="project-button"
                >
                    {project} 
                </button>
            </li>
        ))}
        </ul>
        ) : (
             <p>No {language} projects found.</p>
             )}
                </>) :
                 (
            <p>No language selected.</p>
            )}
        </div>
    );
};

export default SelectProject;
