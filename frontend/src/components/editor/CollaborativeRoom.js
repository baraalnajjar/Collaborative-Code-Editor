import React, { useState, useEffect, useRef } from 'react';
import { useLocation } from 'react-router-dom';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import { request, setAuthHeader } from '../../helpers/axios_helper'; 
import atyponLogo from '../../../src/atypon-logo.png'; 
import '../../CSS/CollaborativeRoom.css'; 

const Collaborative = () => {
    const location = useLocation();
    const { project } = location.state || {};
    const [id, setId] = useState(null);
    const [password, setPassword] = useState(null);
    const [comments, setComments] = useState([]);
    const [loading, setLoading] = useState(true);
    const [newComment, setNewComment] = useState('');
    const [newChatMessage, setNewChatMessage] = useState('');
    const [chatMessages, setChatMessages] = useState([]);
    const [users, setUsers] = useState([]);
    const [projectCode, setProjectCode] = useState(''); 
    const username = localStorage.getItem('username');
    const language = localStorage.getItem('language');
    const [stompClient, setStompClient] = useState(null);
    const [version, setVersion] = useState(''); 
    const [executionOutput, setExecutionOutput] = useState(''); 
    const [versions, setVersions] = useState([]); 
    const [showDropdown, setShowDropdown] = useState(false); 
    const [selectedVersion, setSelectedVersion] = useState(''); 
    const dropdownRef = useRef(null); 

    useEffect(() => {
        const createRoom = async () => {
            const token = localStorage.getItem('auth_token');
            if (!token) {
                alert('Session expired. Please log in again.');
                return;
            }

            setAuthHeader(token);

            try {
                console.log("Creating room...");
                const response = await request('post', '/room/create?projectName='+project);
                console.log("Response received:", response);

                if (response && response.data) {
                    const { password, roomId } = response.data;
                    console.log(`Room created with ID: ${roomId}, Password: ${password}`);
                    setId(roomId);
                    setPassword(password);
                } else {
                    console.error('Unexpected response format:', response);
                    setId(null);
                    setPassword(null);
                }
            } catch (error) {
                console.error('Error creating room:', error);
                setId(null);
                setPassword(null);
            } finally {
                setLoading(false);
            }
        };


        fetchProjectCode(); 
        createRoom();
        fetchComments();

    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [project]);
    useEffect(() => {
        if (id) {
            const socket = new SockJS('http://localhost:8082/app');
            const stomp = Stomp.over(socket);
    
            stomp.connect({}, (frame) => {
                console.log('Connected to WebSocket:', frame);
    
                const chatMessage = {
                    sender: username,
                    password: password,
                };
                stomp.send(`/app/chat.addUser/${id}`, {}, JSON.stringify(chatMessage));
    
                stomp.subscribe(`/topic/room/${id}`, (message) => {
                    if (message.body) {
                        const receivedMessage = JSON.parse(message.body);
                        setChatMessages((prevMessages) => [
                            ...prevMessages,
                            {
                                sender: receivedMessage.sender,
                                content: receivedMessage.content,
                                time: new Date().toLocaleTimeString(),
                            },
                        ]);
                    }
                });
    
                stomp.subscribe(`/topic/users/${id}`, (message) => {
                    if (message.body) {
                        const userUpdate = JSON.parse(message.body);
                        setUsers(userUpdate.users);
                    }
                });
    
                setStompClient(stomp);
            }, (error) => {
                console.error('Error connecting to WebSocket:', error);
            });
    
            return () => {
                stomp.disconnect();
            };
        }
    }, [id, password, username]);   
    useEffect(() => {
        const handleClickOutside = (event) => {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setShowDropdown(false); 
            }
        };

        document.addEventListener('mousedown', handleClickOutside);

        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, [dropdownRef]);
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
    const handleAddComment = async () => {
        const token = localStorage.getItem('auth_token');
        if (!token) {
            alert('Session expired. Please log in again.');
            return;
        }

        setAuthHeader(token);

        try {
            const userIdResponse = await request('GET', '/editor/getId', { editorName: username });
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
    const handleSendChatMessage = () => {
        if (!stompClient) {
            alert('Not connected to WebSocket');
            return;
        }

        const chatMessage = {
            sender: username,
            content: newChatMessage,
            type: 'CHAT'
        };

        stompClient.send(`/app/chat.sendMessage/${id}`, {}, JSON.stringify(chatMessage));

        setNewChatMessage('');
    };
    const handleUserClick = (user) => {
        console.log(`User clicked: ${user}`);
    };
    const handleSaveCode = async () => {
        const requestBody = {
            code: projectCode, 
            version: version, 
            username:username
        };
    
        try {
            const response = await request('POST', 'project/edit?projectName=' + project, requestBody);
            console.log('Code saved successfully:', response.data);
    
            alert('Code saved successfully!');
            fetchProjectCode();
            const chatMessage = {
                sender: username,
                content: "I edited the code, you must fetch!",
                type: 'CHAT'
            };
            stompClient.send(`/app/chat.sendMessage/${id}`, {}, JSON.stringify(chatMessage));
    
        } catch (error) {
            console.error('Error saving code:', error);
            alert('Error saving code. Please try again.');
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
                setVersion(response.data.version);
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
    const handleFetchVersions = async () => {
        const token = localStorage.getItem('auth_token');
        if (!token) {
            alert('Session expired. Please log in again.');
            return;
        }
    
        setAuthHeader(token);
    
        try {
            const response = await request('GET', `/project/${project}/versions`);
            console.log("Fetched versions:", response);
    
            const versionsList = response.data; 
            
            if (versionsList && Array.isArray(versionsList)) {
                setVersions(versionsList); 
                setShowDropdown(true); 
            } else {
                alert('No versions found for the project.');
            }
        } catch (error) {
            console.error('Error fetching versions:', error);
            alert('Failed to fetch versions. Please try again.');
        }
    };
    const handleVersionChange = (event) => {
        const version = event.target.value;
        setSelectedVersion(version); 
        console.log('Selected version:', version);
    };
    const handleRevertVersion = async () => {
        if (!selectedVersion) {
            alert('Please select a version to revert.');
            return;
        }
    
        try {
            const projectResponse = await request('GET', '/project/getId', { project_name: project });
            const projectId = projectResponse.data.id;
    
            const requestBody = {
                projectId: projectId, 
                old_version: selectedVersion,
                username:username
            };
    
            const response = await request('POST', `/project/revert?projectName=${project}`, requestBody);
    
            console.log('Revert response:', response.data);
            alert(`Project reverted to version ${selectedVersion}`);
            fetchProjectCode(); 
            const chatMessage = {
                sender: username,
                content: "I Reverted the code, you must fetch!",
                type: 'CHAT'
            };
            stompClient.send(`/app/chat.sendMessage/${id}`, {}, JSON.stringify(chatMessage));
    
        } catch (error) {
            console.error('Error reverting version:', error);
            alert('Failed to revert version. Please try again.');
        }
    };
    const colors = [
        'green-button',
        'red-button',
        'blue-button',
        'yellow-button',
        'purple-button',
        'orange-button', 
        'teal-button',    
        'pink-button',    
        'brown-button'
    ];
    
    return (
        <div className="collaborative-container">
            <div className="logo-container">
                <img src={atyponLogo} alt="AtYpon Logo" className="atypon-logo" />
            </div>
            
            <div className="user-list-section">
    <h3 style={{ color: 'white' }}>Users in Room</h3>
    {users.length > 0 ? (
        users.map((user, index) => {
            const colorClass = colors[Math.floor(Math.random() * colors.length)];
            return (
                <button
                    key={index}
                    className={`user-button ${colorClass}`}
                    onClick={() => handleUserClick(user)}
                >
                    {user}
                </button>
            );
        })
    ) : (
        <p className="no-users-message">No users online</p>
    )}
</div>


            <div className="project-name-box">
                {project ? `Collaborate on project: ${project}` : 'No Project'}
            </div>
            <div className="id-box">
                {loading ? 'Creating room...' : id ? `ID: ${id}` : 'No ID'}
                <br />
                {loading ? '' : password ? `Password: ${password}` : 'No Password'}
            </div>
            <div className="comments-section">
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

<button className="comments-button" onClick={fetchComments}>
                Fetch Comments
            </button>
            
            </div>
            <div className="add-comment-section">
                <textarea 
                    value={newComment}
                    onChange={(e) => setNewComment(e.target.value)}
                    placeholder="Add comment"
                />
                <button onClick={handleAddComment}>Add Comment</button>
            </div>
            <div className="code-container">
                <textarea
                    className="styled-textarea"
                    value={projectCode || ''} 
                    onChange={(e) => {setProjectCode(e.target.value)} }
                    rows={20} 
                    cols={100} 
                    spellCheck="false" 
                />
                </div>
                <button className="save-button" onClick={handleSaveCode}>Save Code</button>
   
                <button className="run-button" onClick={handleExecuteCode}>Run Code</button>
                <div className="execution-output">
                <h3>Execution Output:</h3>
                <pre>{executionOutput}</pre>
                </div>
                <div>
            <button className="revert-button" onClick={handleFetchVersions}>
                Revert Version
            </button>
            <button className="fetch-code" onClick={fetchProjectCode}>
                Fetch Code
            </button>
            {showDropdown && (
                <div ref={dropdownRef}> {/* Attach the ref here */}
                    <select
                        className="version-dropdown"
                        value={selectedVersion}
                        onChange={handleVersionChange} 
                    >
                        <option value="">Select a version</option>
                        {versions.map((version, index) => (
                            <option key={index} value={version}>
                                {version}
                            </option>
                        ))}
                    </select>

                    <button className="revert-button-v2" onClick={handleRevertVersion}>
                Revert Version
            </button>
                </div>
                
            )}
        </div>
            <div className="chat-section">
                <h3>Live Chat</h3>
                <div className="chat-messages">
                    {chatMessages.length > 0 ? (
                        <ul>
                            {chatMessages.map((msg, index) => (
                                <li key={index}>
                                    <strong>{msg.sender}</strong>: {msg.content} <br />
                                    <small>{msg.date} {msg.time}</small>
                                </li>
                            ))}
                        </ul>
                    ) : (
                        <p>No messages yet.</p>
                    )}
                </div>
                <textarea 
                    value={newChatMessage}
                    onChange={(e) => setNewChatMessage(e.target.value)}
                    placeholder="Type your message here..."
                />
                <button onClick={handleSendChatMessage}>Send</button>
            </div>
            
        </div>
    );
};

export default Collaborative;
