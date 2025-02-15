import { RiRadioButtonLine } from 'react-icons/ri'
import { Container } from './styles'

interface ActivityBadgeProps {
  active: boolean
}

export const ActivityBadge = ({ active }: ActivityBadgeProps) => {
  return (
    <Container $active={active}>
      <RiRadioButtonLine size={12} color={active ? '#00B51B' : '#CF2B2B'} />
      <span>{active ? 'ativo' : 'inativo'}</span>
    </Container>
  )
}
