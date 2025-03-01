/* eslint-disable prettier/prettier */
import { FiMoreHorizontal } from 'react-icons/fi';
import {
  Container,
  LastUpdate,
  Options,
  Row,
  Title,
} from './styles';

interface CardListProps {
  title?: string;
  lastUpdate?: string;
  onClickOptions: () => void;
}

export const CardList = ({
  title = 'ADS 6˚ período',
  lastUpdate = 'Último envio às 11:27',
  onClickOptions,
}: CardListProps) => {

  return (
    <Container>
      <Row>
        <Title>{title}</Title>
        <Options onClick={onClickOptions}>
          <FiMoreHorizontal />
        </Options>
      </Row>
      <Row>
        <LastUpdate>{lastUpdate}</LastUpdate>
      </Row>
    </Container>
  );
};
