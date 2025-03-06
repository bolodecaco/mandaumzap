export interface Message {
  id: string
  broadcastListId: string
  content: string
  sessionId: string
  timesSent: number
  lastSent: string
  url?: string
}
