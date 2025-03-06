import { Column } from '@/lib/styled/global'
import { THEME } from '@/lib/styled/theme'
import { FiRefreshCcw } from 'react-icons/fi'
import { Card, Footer, IconWrapper, Info, Title } from './styles'

interface RoutineCardProps {
  active?: boolean
  title: string
  receivers: string
  days: string
  time: string
  createdAt: string
}

export default function RoutineCard({
  active = false,
  createdAt,
  days,
  receivers,
  time,
  title,
}: RoutineCardProps) {
  return (
    <Card>
      <IconWrapper active={active}>
        <FiRefreshCcw
          size={16}
          color={
            active
              ? THEME.variants.button.primary.color
              : THEME.colors.refreshIcon
          }
        />
      </IconWrapper>
      <Column style={{ gap: '8px' }}>
        <Title>{title}</Title>
        <Info>{receivers}</Info>
        <Info>{days}</Info>
        <Info>{time}</Info>
      </Column>
      <Footer>Criada em {createdAt}</Footer>
    </Card>
  )
}
