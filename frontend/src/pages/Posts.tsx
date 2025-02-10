import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import api from '../api/ApiHandler';
import { Friend } from '../types/Friend';
import '../ui/Posts.css';
import { Post } from '../types/Post';
import { PostComment } from '../types/PostComment';
import MainPanel from '../components/MainPanel';

export const Posts: React.FC = () => {
    const { user } = useAuth();
    const [posts, setPosts] = useState<Post[]>([]);
    const [newPost, setNewPost] = useState<string>('');
    const [newComment, setNewComment] = useState<{ [key: number]: string }>({});
    const navigate = useNavigate();

    const handleNavigate = () => {
        navigate('/main-panel');
    };

    const fetchPosts = async () => {
        try {
            const response = await api.Post('/posts', { email: user?.email });
            const data: Post[] = await response.json();

            for (const post of data) {
                const commentsResponse = await api.Get(`/comments/${post.id}`);
                post.comments = await commentsResponse.json();
            }

            const friendsResponse = await api.Get('/users/friends');
            const friends: Friend[] = await friendsResponse.json();

            for (const friend of friends) {
                const friendPostsResponse = await api.Post('/posts', { email: friend.email });
                const friendPosts: Post[] = await friendPostsResponse.json();

                for (const post of friendPosts) {

                    const commentsResponse = await api.Get(`/comments/${post.id}`);
                    post.comments = await commentsResponse.json();
                }

                data.push(...friendPosts);
            }

            data.sort((a, b) => new Date(b.postedAt).getTime() - new Date(a.postedAt).getTime());
            console.log(data);
            setPosts(data);
        } catch (error) {
            console.error('Error fetching posts:', error);
        }
    };

    const addPost = async () => {
        try {
            const response = await api.Post('/posts/add', { content: newPost });
            const newPostData = await response.json();
            newPostData.liked = false;
            newPostData.comments = [];
            setPosts([newPostData, ...posts]);
            setNewPost('');
        } catch (error) {
            console.error('Error adding post:', error);
        }
    };

    const addComment = async (postId: number) => {
        const commentContent = newComment[postId];
        if (!commentContent) return;

        try {
            const response = await api.Post(`/comments/send/${postId}`, { content: commentContent });
            const newCommentData = await response.json();

            setPosts((prevPosts) =>
                prevPosts.map((post) =>
                    post.id === postId
                        ? { ...post, comments: [...post.comments, newCommentData] }
                        : post
                )
            );

            setNewComment((prev) => ({ ...prev, [postId]: '' }));
        } catch (error) {
            console.error('Error adding comment:', error);
        }
    };

    const toggleLikePost = async (postId: number) => {
        try {
            const response = await api.Post(`/posts/toggle-like/${postId}`);
            const data = await response.json();
            console.log(data);
        }
        catch (error) {
            console.error('Error toggling like:', error);
            return
        }
        setPosts((prevPosts) =>
            prevPosts.map((post) =>
                post.id === postId
                    ? { ...post, liked: !post.liked, likes: post.liked ? post.likes - 1 : post.likes + 1 }
                    : post
            )
        );
    };

    useEffect(() => {
        fetchPosts();
    }, []);

    const deletePost = async (postId: number) => {
        try {
            await api.Delete(`/posts/remove/${postId}`);
            setPosts((prevPosts) => prevPosts.filter((post) => post.id !== postId));
        } catch (error) {
            console.error('Error deleting post:', error);
        }
    };

    const deleteComment = async (postId: number, commentId: number) => {
        try {
            await api.Delete(`/comments/remove/${commentId}`);
            setPosts((prevPosts) =>
                prevPosts.map((post) =>
                    post.id === postId
                        ? { ...post, comments: post.comments.filter((comment) => comment.id !== commentId) }
                        : post
                )
            );
        } catch (error) {
            console.error('Error deleting comment:', error);
            return;
        }
    };

    const toggleLikeComment = async (postId: number, commentId: number) => {
        try {
            const response = await api.Post(`/comments/toggle-like/${commentId}`);
            const data = await response.json();
            console.log(data);
        } catch (error) {
            console.error('Error toggling like:', error);
        }
        setPosts((prevPosts) =>
            prevPosts.map((post) =>
                post.id === postId
                    ? {
                          ...post,
                          comments: post.comments.map((comment) =>
                              comment.id === commentId
                                  ? {
                                        ...comment,
                                        liked: !comment.liked,
                                        likes: comment.liked ? comment.likes - 1 : comment.likes + 1,
                                    }
                                  : comment
                          ),
                      }
                    : post
            )
        );
    };

    return (
        <div className="posts-container">
            <MainPanel />
            <h2 className="posts-title">Posts</h2>
            <form
                className="message-form"
                onSubmit={(e) => {
                    e.preventDefault();
                    addPost();
                }}
            >
                <input
                    type="text"
                    placeholder="What’s on your mind?"
                    value={newPost}
                    onChange={(e) => setNewPost(e.target.value)}
                    style={{ width: '100%' }}
                />
                <button type="submit">Post</button>
            </form>

            {posts.map((post) => (
                <div key={post.id} className="post-card">
                    <div className="post-header">
                        <strong className="poster-name">{post.poster.name}</strong>
                        <span className="post-date">{new Date(post.postedAt).toLocaleString()}</span>
                        {user?.email === post.poster.email && (
                            <button onClick={() => deletePost(post.id)} className="delete-button">
                                Delete Post
                            </button>
                        )}
                    </div>
                    <div className="post-content">{post.content}</div>
                    <div className="post-actions">
                        <button onClick={() => toggleLikePost(post.id)} className="like-button">
                            {post.liked ? '❤️' : '🤍'} {post.likes}
                        </button>
                    </div>

                    <div className="post-comments">
                        <h4>Comments:</h4>
                        {post.comments.map((comment) => (
                            <div key={comment.id} className="comment">
                                <div className="comment-header">
                                    <strong className="commenter-name">{comment.commenter.name}</strong>
                                    <span className="comment-date" style={{ textAlign: 'center', flex: 1 }}>
                                        {new Date(comment.commentedAt).toLocaleString()}
                                    </span>
                                    {user?.email === comment.commenter.email && (
                                        <button
                                            onClick={() => deleteComment(post.id, comment.id)}
                                            className="delete-comment-button"
                                            style={{ marginLeft: 'auto' }}
                                        >
                                            Delete Comment
                                        </button>
                                    )}
                                </div>
                                <div className="comment-content">{comment.content}</div>
                                <div className="comment-actions">
                                    <button
                                        onClick={() => toggleLikeComment(post.id, comment.id)}
                                        className="like-button"
                                    >
                                        {comment.liked ? '❤️' : '🤍'} {comment.likes}
                                    </button>
                                </div>
                            </div>
                        ))}
                        <form
                            className="comment-form"
                            onSubmit={(e) => {
                                e.preventDefault();
                                addComment(post.id);
                            }}
                            style={{ width: '100%' }}
                        >
                            <input
                                type="text"
                                placeholder="Add a comment"
                                value={newComment[post.id] || ''}
                                onChange={(e) =>
                                    setNewComment((prev) => ({ ...prev, [post.id]: e.target.value }))
                                }
                                style={{ width: '100%' }}
                            />
                            <button type="submit">Comment</button>
                        </form>
                    </div>
                </div>
            ))}
        </div>
    );
};

export default Posts;
