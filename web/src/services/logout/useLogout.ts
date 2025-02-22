import { getSession, signOut } from 'next-auth/react'
import { redirect } from 'next/navigation'
import { toast } from 'react-toastify'

export const handleLogout = async () => {
  const session = await getSession()
  const idToken = session?.idToken

  if (!idToken) {
    toast.error('Erro ao fazer logout. Tente recarregar a página.')
    return
  }

  const redirectUri = process.env.NEXT_PUBLIC_NEXTAUTH_URL
  const kcIssuer = process.env.NEXT_PUBLIC_KC_ISSUER

  await signOut({ redirect: false })

  const keycloakLogoutUrl = `${kcIssuer}/protocol/openid-connect/logout?post_logout_redirect_uri=${redirectUri}&id_token_hint=${encodeURIComponent(idToken)}`

  redirect(keycloakLogoutUrl)
}
