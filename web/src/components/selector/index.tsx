import { FiChevronDown, FiChevronUp } from 'react-icons/fi'
import { Icon, Select } from '@radix-ui/react-select'
import { useMemo, useState } from 'react'
import { Container, Value, Popper, StyledItem } from './styles'
import Skeleton from 'react-loading-skeleton'

type Option = {
  id: number | string
  value: string
  name: string
}

interface SelectorProps {
  label: string
  options: Option[]
  width?: string
  height?: string
  value: string
  isLoading?: boolean
  onValueChange: (newValue: string) => void
}

export const Selector = ({
  label,
  width,
  height,
  options,
  isLoading,
  value,
  onValueChange,
}: SelectorProps) => {
  const [open, setOpen] = useState(false)
  const selectedValue = useMemo(
    () => options.find((option) => option.value === value)?.name,
    [options, value],
  )

  if (isLoading) {
    return <Skeleton width={width ?? '8rem'} height={height ?? '2.5rem'} />
  }

  return (
    <Select
      open={open}
      value={value}
      onOpenChange={() => setOpen(!open)}
      onValueChange={(newValue) => onValueChange(newValue)}
    >
      <Container $width={width} $height={height}>
        <Value $placeholder={!!selectedValue}>{selectedValue || label}</Value>
        <Icon>
          {!open ? <FiChevronDown size={20} /> : <FiChevronUp size={20} />}
        </Icon>
      </Container>

      <Popper position="popper">
        {options.map((option) => (
          <StyledItem key={option.id} value={option.value}>
            {option.name}
          </StyledItem>
        ))}
      </Popper>
    </Select>
  )
}
