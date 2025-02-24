/* eslint-disable prettier/prettier */
import { FiMoreHorizontal } from 'react-icons/fi'

import {
    Avatar,
    Avatars,
    Container,
    LastUpdate,
    MoreAvatars,
    Options,
    Row,
    Title,
} from './styles'




interface CardListProps {
  title: string
  lastUpdate: string
  avatars: { src: string }[]
  onClickOptions: () => void
}

export const CardList = ({
  title = 'ADS 6˚ periodo',
  lastUpdate = 'Último envio às 11:27',
  avatars,
  onClickOptions,
}: CardListProps) => {


  const displayedAvatars = avatars.slice(0, 3)
  const moreCount = avatars.length - displayedAvatars.length

  return (
    <Container>
      <Row>
        <Title>{title}</Title>
        <Options onClick={() => onClickOptions()}>
          <FiMoreHorizontal />
        </Options>
      </Row>
      <Row>
        <Avatars>
          {displayedAvatars.map((avatar, index) => (
            <Avatar key={index} src={avatar.src} />
          ))}
          {moreCount > 0 && <MoreAvatars>{moreCount}+</MoreAvatars>}
        </Avatars>
        <LastUpdate>{lastUpdate}</LastUpdate>
      </Row>
    </Container>
  )
}
