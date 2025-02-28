import LetteredAvatar from 'react-lettered-avatar'
import { Avatar } from '../avatar'
import { Checkbox, Container, Session, UserDiv } from './styles'

interface CardContactProps {
  name: string
  session: string
  contact: string
  onCheck?: () => void
}

export const CardContact = ({
  session,
  name,
  contact,
  onCheck,
}: CardContactProps) => {
  const normalizedContact = contact.includes('@s.whatsapp.net')
    ? contact.replace('@s.whatsapp.net', '')
    : '-'

  return (
    <Container>
      <Checkbox type="checkbox" onClick={onCheck} />
      <UserDiv>
        {name === 'Desconhecido' ? (
          <Avatar />
        ) : (
          <LetteredAvatar
            size={34}
            name={name[0]}
            color="#fff"
            backgroundColor="#075E54"
          />
        )}
        {name}
      </UserDiv>
      <Session>{normalizedContact}</Session>
      <Session>{session}</Session>
    </Container>
  )
}
