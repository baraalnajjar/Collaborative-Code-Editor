import React, { useState, useEffect } from 'react';
import '../../CSS/ViewProject.css';
import { request, setAuthHeader } from '../../helpers/axios_helper'; 
import atyponLogo from '../../../src/atypon-logo.png'; 

const ViewProject = () => {
    const [comments, setComments] = useState([]);
    const [newComment, setNewComment] = useState('');
    const [logs, setLogs] = useState([]); // State for logs
    const [projectCode, setProjectCode] = useState(''); 
    const username = localStorage.getItem('username');
    const language = localStorage.getItem('language');
    const [executionOutput, setExecutionOutput] = useState(''); 
    const project = localStorage.getItem('project');

    useEffect(() => {
        fetchProjectCode(); 
        fetchComments();
        fetchLogs();
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [project]);

    const fetchComments = async () => {
        const token = localStorage.getItem('auth_token');
        if (!token) {
            alert('Session expired. Please log in again.');
            return;
        }

        setAuthHeader(token);

        try {
            console.log("Fetching project ID...");
            const projectResponse = await request('GET', '/project/getId', { project_name: project });
            console.log("Project ID response:", projectResponse);

            const projectId = projectResponse.data.id;

            console.log("Fetching comments...");
            const commentsResponse = await request('GET', '/comment/read', { project_id: projectId });
            console.log("Comments response:", commentsResponse);

            if (commentsResponse && commentsResponse.data && commentsResponse.data.comments) {
                setComments(commentsResponse.data.comments);
            } else {
                console.error('No comments found:', commentsResponse);
                setComments([]);
            }
        } catch (error) {
            console.error('Error fetching comments:', error);
            setComments([]);
        }
    };

    const fetchLogs = async () => {
        const token = localStorage.getItem('auth_token');
        if (!token) {
            alert('Session expired. Please log in again.');
            return;
        }

        setAuthHeader(token);

        try {
            console.log("Fetching logs...");
            const logsResponse = await request('GET', '/project/logs?projectName=' + project);
            console.log("Logs response:", logsResponse);

            if (logsResponse && logsResponse.data) {
                setLogs(logsResponse.data); // Set logs directly from response
            } else {
                console.error('No logs found:', logsResponse);
                setLogs([]);
            }
        } catch (error) {
            console.error('Error fetching logs:', error);
            setLogs([]);
        }
    };

    const handleAddComment = async () => {
        const token = localStorage.getItem('auth_token');
        if (!token) {
            alert('Session expired. Please log in again.');
            return;
        }

        setAuthHeader(token);

        try {
            const userIdResponse = await request('GET', '/viewer/getId', { viewerName: username });
            const userId = userIdResponse.data;

            const projectResponse = await request('GET', '/project/getId', { project_name: project });
            const projectId = projectResponse.data.id;

            const commentBody = {
                comment: newComment,
                project_id: projectId
            };

            await request('POST', `/comment/post?userID=${userId}`, commentBody);

            setComments((prevComments) => [
                ...prevComments,
                { name: username, comment: newComment, date: new Date().toLocaleDateString(), time: new Date().toLocaleTimeString() }
            ]);

            setNewComment('');
        } catch (error) {
            console.error('Error adding comment:', error);
            alert('Failed to add comment. Please try again.');
        }
    };

    const fetchProjectCode = async () => {
        const token = localStorage.getItem('auth_token');
        
        if (!token) {
            alert('Session expired. Please log in again.');
            return;
        }

        setAuthHeader(token);

        try {
            console.log("Fetching project code...");

            const response = await request('GET', '/project/getCode', { project_name: project });

            console.log("Project code response:", response);

            if (response && response.data) {
                setProjectCode(response.data.code);
                alert('Project code fetched successfully!');
            } else {
                console.error('Code not found:', response);
                setProjectCode('');
            }
        } catch (error) {
            console.error('Error fetching project code:', error);
            setProjectCode('');
        }
    };

    const handleExecuteCode = async () => {
        const requestBody = {
            code: projectCode, 
            className: project, 
        };

        try {
            const response = await request('POST', '/project/execute?language=' + language, requestBody);
            console.log('Code executed successfully:', response.data);
            setExecutionOutput(response.data); 
        } catch (error) {
            console.error('Error executing code:', error);
            setExecutionOutput('Error executing code: ' + error.message); 
        }
    };
    
    return (
        <div className="view-container-v2">
            <div className="logo-container-v2">
                <img src={atyponLogo} alt="AtYpon Logo" className="atypon-logo" />
            </div>

            <div className="project-name-box-v2">
                {project ? `Project Name: ${project}` : 'No Project'}
            </div>

            <div className="comments-section-v2">
                <h3>Comments</h3>
                {comments.length > 0 ? (
                    <ul>
                        {comments.map((comment, index) => (
                            <li key={index}>
                                <strong>{comment.name}</strong>: {comment.comment} <br />
                                <small>{comment.date} {comment.time}</small>
                            </li>
                        ))}
                    </ul>
                ) : (
                    <p>No comments found.</p>
                )}
                <button className="comments-button-v2" onClick={fetchComments}>Fetch Comments</button>
            </div>

            <div className="add-comment-section-v2">
                <textarea 
                    value={newComment}
                    onChange={(e) => setNewComment(e.target.value)}
                    placeholder="Add comment"
                />
                <button onClick={handleAddComment}>Add Comment</button>
            </div>

            <div className="code-container-v2">
                <pre className="styled-code-block">
                    {projectCode || 'No code available'}
                </pre>
            </div>

            <button className="run-button-v2" onClick={handleExecuteCode}>Run Code</button>

            <div className="execution-output-v2">
                <h3>Execution Output:</h3>
                <pre>{executionOutput}</pre>
            </div>

            <div>
                <button className="fetch-code-v2" onClick={fetchProjectCode}>
                    Fetch Code
                </button>
            </div>    

            <div className="logs-v2">
                <h3>Logs:</h3>
                {logs.length > 0 ? (
                    <ul>
                        {logs.map((log, index) => (
                            <li key={index}>
                                <strong>Username:</strong> {log.username} <br />
                                <strong>Date:</strong> {log.date} <br />
                                <strong>Time:</strong> {log.time} <br />
                                <strong>Version:</strong> {log.version} <br />
                                <strong>Edit Type:</strong> {log.editType} <br />
                            </li>
                        ))}
                    </ul>
                ) : (
                    <p>No logs found.</p>
                )}
            </div>
        </div>
    );
};

export default ViewProject;
