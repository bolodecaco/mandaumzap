'use client'

import { useMemo } from 'react'
import { Badge, Container, Description, Title } from './styles'
import { Row } from '../cardList/styles'
import { ProgressBar } from '../progressBar'
import { Button } from '../button'
import { FaCheck } from 'react-icons/fa'
import { readNotifications } from '@/app/actions/notifications/readNotification'
import { toast } from 'react-toastify'

interface NotificationCardProps {
  id: string
  totalChats: number
  sentChats: number
  onRead: () => void
}

export const NotificationCard = ({
  id,
  totalChats,
  sentChats,
  onRead,
}: NotificationCardProps) => {
  const progress = useMemo(() => {
    if (totalChats === 0) return 0
    return (sentChats / totalChats) * 100
  }, [totalChats, sentChats])

  const title = useMemo(
    () => (progress === 100 ? 'Finalizado!' : 'Quase lá!'),
    [progress],
  )

  const description = useMemo(
    () =>
      progress === 100
        ? 'Mensagem enviada com sucesso para todos os contatos da lista selecionada!'
        : 'Sua mensagem está sendo enviada para todos os contatos da lista selecionada.',
    [progress],
  )

  const handleReadNotification = async () => {
    const response = await readNotifications({ id })
    if (!response.success) {
      toast.error('Erro ao marcar notificação como lida. Tente novamente.', {
        toastId: 'notificationError',
      })
      return
    }
    onRead()
  }

  return (
    <Container>
      <Row style={{ alignItems: 'center' }}>
        <Title>{title}</Title>
        <Badge>{progress.toFixed(0)}%</Badge>
      </Row>
      <Description>{description}</Description>
      <ProgressBar progress={progress} />
      {progress === 100 && (
        <Button
          onClick={handleReadNotification}
          leftIcon={FaCheck}
          iconSize={16}
          style={{
            marginLeft: 'auto',
            width: '2rem',
            height: '2rem',
            padding: 0,
          }}
          iconColor="#ffffff"
        />
      )}
    </Container>
  )
}
