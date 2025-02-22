import { useState } from 'react'
import { FiChevronDown } from 'react-icons/fi'
import { Container, SelectorButton, SelectorItem, SelectorMenu } from './styles'

interface SelectorProps {
  label: string
  options: string[]
  width?: string
  height?: string
  onSelect: (option: string) => void
}

export const Selector = ({
  label,
  width,
  height,
  options,
  onSelect,
}: SelectorProps) => {
  const [open, setOpen] = useState(false)

  //   const [selectedLabel, setSelectedLabel] = useState(label)

  const handleSelect = (option: string) => {
    // setSelectedLabel(option)
    onSelect(option)
    setOpen(false)
  }

  return (
    <Container>
      <SelectorButton
        $width={width}
        $height={height}
        onClick={() => setOpen(!open)}
      >
        {label} <FiChevronDown />
      </SelectorButton>
      <SelectorMenu open={open}>
        {options.map((option) => (
          <SelectorItem
            key={option}
            onClick={() => {
              handleSelect(option)
            }}
          >
            {option}
          </SelectorItem>
        ))}
      </SelectorMenu>
    </Container>
  )
}
