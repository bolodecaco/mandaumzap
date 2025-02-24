import { FiChevronDown } from 'react-icons/fi'
import { Container, SelectorButton, SelectorItem, SelectorMenu } from './styles'

interface SelectorProps {
  label: string
  options: string[]
  width?: string
  height?: string
  onSelect: (option: string) => void
  isOpen: boolean
  onOpenChange: () => void
}

export const Selector = ({
  label,
  width,
  height,
  options,
  onSelect,
  isOpen,
  onOpenChange,
}: SelectorProps) => {
  const handleSelect = (option: string) => {
    onSelect(option)
    onOpenChange()
  }

  return (
    <Container>
      <SelectorButton $width={width} $height={height} onClick={onOpenChange}>
        {label} <FiChevronDown />
      </SelectorButton>
      <SelectorMenu open={isOpen}>
        {options.map((option) => (
          <SelectorItem key={option} onClick={() => handleSelect(option)}>
            {option}
          </SelectorItem>
        ))}
      </SelectorMenu>
    </Container>
  )
}
