import { Column, Row } from '@/lib/styled/global'
import { THEME } from '@/lib/styled/theme'
import { BsPhone } from 'react-icons/bs'
import { ActivityBadge } from '../activityBadge'
import { Delete, Phone, Uptime } from './styles'
import { BiTrash } from 'react-icons/bi'
import { deleteSession } from '@/app/actions/sessions/deleteSession'
import { useQueryClient } from '@tanstack/react-query'
import { toast } from 'react-toastify'
import { Session } from '@/@types/session'
import { useState } from 'react'
import { Confirmation } from '../modal/confirmation'

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
  const [isConfirmationModalOpen, setIsConfirmationModalOpen] = useState(false)
  const queryClient = useQueryClient()

  const handleOpenModal = () => {
    setIsConfirmationModalOpen(true)
  }

  const handleCloseModal = () => {
    setIsConfirmationModalOpen(false)
  }

  const handleDelete = async () => {
    const response = await deleteSession(id)
    if (response.success) {
      queryClient.invalidateQueries({
        queryKey: ['sessions'],
        refetchType: 'active',
      })
      toast.success('Dispositivo desconectado com sucesso')
      handleCloseModal()
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
        onClick={handleOpenModal}
        style={{ marginLeft: 'auto' }}
        variant="ghost"
        leftIcon={BiTrash}
        iconSize={24}
        iconColor={THEME.colors.tertiary}
      />

      {isConfirmationModalOpen && (
        <Confirmation
          cancelButtonText="Cancelar"
          confirmButtonText="Sim, desconectar dispositivo"
          title="Tem certeza que deseja desconectar o dispositivo?"
          content={
            <span>
              Esta ação irá desconectar o dispositivo e irá excluir todos os
              chats disponíveis vinculados a ele. Você precisará reconectar o
              dispositivo para recuperar os chats.
            </span>
          }
          onCancelButtonClick={handleCloseModal}
          onCloseButtonClick={handleCloseModal}
          onConfirmButtonClick={handleDelete}
        />
      )}
    </Row>
  )
}
