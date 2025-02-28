/* eslint-disable prettier/prettier */
import { FiMoreHorizontal } from 'react-icons/fi';
import LetteredAvatar from 'react-lettered-avatar';
import {
  Avatars,
  Container,
  DivAvatar,
  LastUpdate,
  MoreAvatars,
  Options,
  Row,
  Title,
} from './styles';

interface CardListProps {
  title?: string;
  lastUpdate?: string;
  avatars: { name: string }[];
  onClickOptions: () => void;
}

export const CardList = ({
  title = 'ADS 6˚ período',
  lastUpdate = 'Último envio às 11:27',
  avatars,
  onClickOptions,
}: CardListProps) => {
  const displayedAvatars = avatars.slice(0, 3);
  const moreCount = avatars.length - displayedAvatars.length;

  return (
    <Container>
      <Row>
        <Title>{title}</Title>
        <Options onClick={onClickOptions}>
          <FiMoreHorizontal />
        </Options>
      </Row>
      <Row>
        <Avatars>
          {displayedAvatars.map((avatar, index) => (
            <DivAvatar key={index}>
              <LetteredAvatar size={34} name={avatar.name} />
            </DivAvatar>
          ))}
          {moreCount > 0 && <MoreAvatars>{moreCount > 9 ? '9+' : `${moreCount}+`}</MoreAvatars>}
        </Avatars>
        <LastUpdate>{lastUpdate}</LastUpdate>
      </Row>
    </Container>
  );
};
