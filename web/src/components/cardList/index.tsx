/* eslint-disable prettier/prettier */
import { FiMoreHorizontal } from 'react-icons/fi';
import { Button } from '../button';
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
  onSelected?: () => void
  buttonText?: string;
}

export const CardList = ({
  title = 'ADS 6˚ período',
  lastUpdate = 'Último envio às 11:27',
  buttonText,
  onClickOptions,
  onSelected
}: CardListProps) => {

  return (
    <Container>
      <Row>
        <Title>{title}</Title>
        {!onSelected && (
          <Options onClick={onClickOptions}>
            <FiMoreHorizontal />
          </Options>
        )}
      </Row>
      {!onSelected && (
        <Row>
          <LastUpdate>{lastUpdate}</LastUpdate>
        </Row>    
      )}
      {onSelected && <Button text={buttonText || 'Adicionar'} onClick={onSelected} variant='primary' style={{ width: '6.5rem'}}/>}
    </Container>
  );
};
