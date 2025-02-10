export interface Message {
    content: string; 
    id: number; 
    liked: boolean; 
    numberOfLikes: number;
    senderName: string;
    sentAt: string;
}