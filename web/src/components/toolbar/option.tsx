import { ComponentProps } from 'react'
import { Wrapper } from './styles'
import Image from 'next/image'

interface OptionProps extends ComponentProps<'button'> {
  iconName: string
  iconAlt: string
}

export const Option = ({ iconName, iconAlt, ...rest }: OptionProps) => {
  return (
    <Wrapper {...rest}>
      <Image
        src={`/assets/toolbar/${iconName}.svg`}
        width={24}
        height={24}
        alt={iconAlt}
      />
    </Wrapper>
  )
}
