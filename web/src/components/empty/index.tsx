import { Column } from '@/lib/styled/global'
import { ElementType } from 'react'
import { Action, Container, Message } from './styles'

interface EmptyProps {
  message: string
  action?: string
  onActionClick?: () => void
  icon: ElementType
}

export const Empty = ({
  message,
  action,
  onActionClick,
  icon: Icon,
}: EmptyProps) => (
  <Container>
    {Icon && <Icon size={48} color="#878787" />}
    <Column>
      <Message>{message}</Message>
      {action && <Action onClick={onActionClick}>{action}</Action>}
    </Column>
  </Container>
)
