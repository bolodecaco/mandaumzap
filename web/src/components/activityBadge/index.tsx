import { RiRadioButtonLine } from 'react-icons/ri'
import { Container } from './styles'
import { THEME } from '@/lib/styled/theme'
import { Session } from '@/@types/session'

interface ActivityBadgeProps {
  status: Session['status']
}

const STATUS_MAP = {
  pending: {
    title: 'Pendente',
    color: THEME.colors.secondary,
  },
  close: {
    title: 'Fechada',
    color: THEME.colors.tertiary,
  },
  open: {
    title: 'Aberta',
    color: THEME.colors.primaryForeground,
  },
  error: {
    title: 'Com erro',
    color: THEME.colors.border,
  },
}

export const ActivityBadge = ({ status }: ActivityBadgeProps) => {
  return (
    <Container
      style={{
        borderColor: STATUS_MAP[status].color,
        color: STATUS_MAP[status].color,
      }}
    >
      <RiRadioButtonLine size={12} color={STATUS_MAP[status].color} />
      <span>{STATUS_MAP[status].title}</span>
    </Container>
  )
}
