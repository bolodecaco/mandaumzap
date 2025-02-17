type Status = 'pending' | 'closed' | 'open'

export interface Session {
  id: string
  active: boolean
  message?: string
  qrcode?: string
  status: Status
}
