import NextAuth from 'next-auth'
import Keycloak from 'next-auth/providers/keycloak'
import { authConfig } from './auth.config'

export const { signIn, signOut, auth, handlers } = NextAuth({
  ...authConfig,
  trustHost: true,
  providers: [
    Keycloak({
      clientId: process.env.NEXT_PUBLIC_KC_CLIENT_ID,
      clientSecret: process.env.KC_CLIENT_SECRET,
      issuer: process.env.NEXT_PUBLIC_KC_ISSUER,
    }),
  ],
  callbacks: {
    async jwt({ token, account, profile }) {
      if (account && profile) {
        token.accessToken = account.access_token
        token.refreshToken = account.refresh_token
        token.uuid = profile.sub
        token.idToken = account.id_token
      }
      return token
    },
    async session({ session, token }) {
      session.accessToken = token.accessToken
      session.refreshToken = token.refreshToken
      session.uuid = token.uuid
      session.idToken = token.idToken
      return session
    },
  },
})
