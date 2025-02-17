import { Column, Row } from '@/lib/styled/global'
import { THEME } from '@/lib/styled/theme'
import { BsPhone } from 'react-icons/bs'
import { ActivityBadge } from '../activityBadge'
import { Delete, Phone, Uptime } from './styles'
import { BiTrash } from 'react-icons/bi'
import { deleteSession } from '@/app/actions/sessions/deleteSession'
import { QueryClient } from '@tanstack/react-query'
import { toast } from 'react-toastify'
import { Session } from '@/@types/session'

interface DeviceProps {
  status: Session['status']
  id: string
}

const STATUS_COLOR_MAP = {
  pending: THEME.colors.secondary,
  close: THEME.colors.tertiary,
  open: THEME.colors.primaryForeground,
  error: THEME.colors.background,
}

export const Device = ({ status, id }: DeviceProps) => {
  const handleDelete = async (sessionId: string) => {
    const response = await deleteSession(sessionId)
    if (response.success) {
      const queryClient = new QueryClient()
      queryClient.refetchQueries({
        queryKey: ['sessions'],
        type: 'active',
      })
      toast.success('Dispositivo desconectado com sucesso')
    } else {
      toast.error(response.error)
    }
  }

  return (
    <Row style={{ gap: '0.15rem', alignItems: 'center' }}>
      <BsPhone
        size={64}
        color={STATUS_COLOR_MAP[status as Session['status']]}
      />
      <Column style={{ gap: '0.5rem' }}>
        <ActivityBadge status={status} />
        <Phone>{id.slice(0, 8)}</Phone>
        <Uptime>conectado há 3 dias</Uptime>
      </Column>
      <Delete
        // abrir modal de confirmação
        style={{ marginLeft: 'auto' }}
        variant="ghost"
        leftIcon={BiTrash}
        iconSize={24}
        iconColor={THEME.colors.tertiary}
      />
    </Row>
  )
}
