type Status = 'pending' | 'close' | 'open' | 'error'

export interface Session {
  id: string
  status: Status
  qrcode?: string
}
