import { BiLogOut } from 'react-icons/bi'
import { Container } from './styles'
import { THEME } from '@/lib/styled/theme'
import { handleLogout } from '@/services/logout/useLogout'

export const Logout = () => {
  return (
    <Container
      leftIcon={BiLogOut}
      iconSize={24}
      iconColor={THEME.colors.tertiary}
      onClick={handleLogout}
      variant="ghost"
    />
  )
}
