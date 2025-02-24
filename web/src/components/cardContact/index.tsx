import { Avatar, Checkbox, Container, ListName, Phone, UserDiv } from './styles'

interface CardContactProps {
  avatar: string
  name: string
  phone: string
  list: string
  checked: boolean
  onCheck?: () => void
}

export const CardContact: React.FC<CardContactProps> = ({
  avatar,
  name,
  phone,
  list,
  checked,
  onCheck,
}) => (
  <Container>
    <Checkbox type="checkbox" checked={checked} onChange={onCheck} />
    <UserDiv>
      <Avatar src={avatar} alt={`Avatar de ${name}`} />
      <span>{name}</span>
    </UserDiv>
    <Phone>{phone}</Phone>
    <ListName>{list}</ListName>
  </Container>
)
