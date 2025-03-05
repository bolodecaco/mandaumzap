export interface Notification {
  id: string
  receiverId: string
  type: 'progress'
  read: boolean
  content: string
}

export interface ParsedContent {
  messageId: string
  userId: string
  sentChats: number
  unsentChats: number
  totalChats: number
  sessionId?: string | null
  text?: string | null
  receivers?: [] | null
  url?: string | null
}
