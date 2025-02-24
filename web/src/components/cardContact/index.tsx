import LetteredAvatar from 'react-lettered-avatar'
import { Avatar } from '../avatar'
import {
  Checkbox,
  Container,
  DivAvatar,
  ListName,
  Phone,
  UserDiv,
} from './styles'

interface CardContactProps {
  name: string
  phone: string
  list: string
  onCheck?: () => void
}

export const CardContact = ({
  list,
  name,
  phone,
  onCheck,
}: CardContactProps) => {
  return (
    <Container>
      <Checkbox type="checkbox" onClick={onCheck} />
      <UserDiv>
        {name === 'Desconhecido' ? (
          <Avatar />
        ) : (
          <DivAvatar>
            <LetteredAvatar size={34} name={name} />
          </DivAvatar>
        )}
        {name}
      </UserDiv>
      <Phone>{phone}</Phone>
      <ListName>{list}</ListName>
    </Container>
  )
}
