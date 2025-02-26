import { create } from 'zustand'
import { Session } from '@/@types/session'
import { deleteAllSessions } from '@/app/actions/sessions/deleteAllSessions'
import { toast } from 'react-toastify'

interface SessionStore {
  sessions: Session[]
  isLoading: boolean
  setSessions: (sessions: Session[]) => void
  setLoading: (loading: boolean) => void
  removeAllSessions: () => Promise<void>
}

export const useSessionStore = create<SessionStore>((set) => ({
  sessions: [],
  isLoading: false,
  setSessions: (sessions) => set({ sessions }),
  setLoading: (loading) => set({ isLoading: loading }),
  removeAllSessions: async () => {
    const response = await deleteAllSessions()
    if (response.success) {
      set({ sessions: [] })
      toast.success('Todos os dispositivos foram desconectados.')
    } else {
      toast.error(response.error)
    }
  },
}))
