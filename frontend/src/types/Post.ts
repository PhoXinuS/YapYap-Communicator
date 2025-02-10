import { PostComment } from './PostComment';
 
 
export interface Post {
    id: number;
    content: string;
    postedAt: string;
    poster: { email?: string; name?: string };
    likes: number;
    liked: boolean;
    comments: PostComment[];
}