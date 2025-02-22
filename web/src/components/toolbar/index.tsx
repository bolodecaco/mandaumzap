import { ButtonGroup, Container, Divider } from './styles'
import { Option } from './option'

interface ToolbarProps {
  onApplyFormatting: (format: string) => void
}

export const Toolbar = ({ onApplyFormatting }: ToolbarProps) => {
  return (
    <Container>
      <ButtonGroup>
        <Option
          onClick={() => onApplyFormatting('bold')}
          iconName="bold"
          iconAlt="ícone de negrito"
        />
        <Option
          onClick={() => onApplyFormatting('italic')}
          iconName="italic"
          iconAlt="ícone de itálico"
        />
        <Option
          onClick={() => onApplyFormatting('strike')}
          iconName="strike"
          iconAlt="ícone de tachado"
        />
      </ButtonGroup>

      <Divider />

      <ButtonGroup>
        <Option
          onClick={() => onApplyFormatting('quote')}
          iconName="quote"
          iconAlt="ícone de citação"
        />
        <Option
          onClick={() => onApplyFormatting('code')}
          iconName="code"
          iconAlt="ícone de trecho de código"
        />
      </ButtonGroup>

      <Divider />

      <ButtonGroup>
        <Option
          //   onClick={() => onApplyFormatting('emoji')}
          iconName="emoji"
          iconAlt="ícone de emoji"
        />
        <Option
          //   onClick={() => onApplyFormatting('image')}
          iconName="image"
          iconAlt="ícone de imagem"
        />
      </ButtonGroup>
    </Container>
  )
}
