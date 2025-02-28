declare module 'react-lettered-avatar' {
  import { ComponentType } from 'react'

  interface LetteredAvatarProps {
    name?: string
    size?: number
    color?: string
    backgroundColor?: string
    radius?: number
  }

  const content: ComponentType<LetteredAvatarProps>
  export default content
}
