import { useEffect, useRef } from 'react'
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
  const containerRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (
        containerRef.current &&
        !containerRef.current.contains(event.target as Node)
      ) {
        if (isOpen) {
          onOpenChange()
        }
      }
    }

    document.addEventListener('mousedown', handleClickOutside)
    return () => document.removeEventListener('mousedown', handleClickOutside)
  }, [isOpen, onOpenChange])

  const handleSelect = (option: string) => {
    onSelect(option)
    onOpenChange()
  }

  return (
    <Container ref={containerRef}>
      <SelectorButton $width={width} $height={height} onClick={onOpenChange}>
        {label} <FiChevronDown size={20} />
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
