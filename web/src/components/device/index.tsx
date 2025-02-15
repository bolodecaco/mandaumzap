import { Column, Row } from '@/lib/styled/global'
import { THEME } from '@/lib/styled/theme'
import { BsPhone } from 'react-icons/bs'
import { ActivityBadge } from '../activityBadge'
import { Phone, Uptime } from './styles'

interface DeviceProps {
  active: boolean
  id: string
}

export const Device = ({ active, id }: DeviceProps) => (
  <Row style={{ gap: '0.15rem', alignItems: 'center' }}>
    <BsPhone
      size={64}
      color={active ? THEME.colors.primary : THEME.colors.secondary}
    />
    <Column style={{ gap: '0.5rem' }}>
      <ActivityBadge active={active} />
      <Phone>{id.slice(0, 8)}</Phone>
      <Uptime>conectado há 3 dias</Uptime>
    </Column>
  </Row>
)
