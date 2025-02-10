export interface PostComment {
    id: number;
    content: string;
    commentedAt: string;
    commenter: { email?: string; name?: string };
    likes: number;
    liked: boolean;
}