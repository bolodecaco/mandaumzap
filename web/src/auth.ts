import NextAuth from 'next-auth'
import Keycloak from 'next-auth/providers/keycloak'
import { authConfig } from './auth.config'

export const { signIn, signOut, auth, handlers } = NextAuth({
  ...authConfig,
  providers: [
    Keycloak({
      clientId: process.env.KC_CLIENT_ID,
      clientSecret: process.env.KC_CLIENT_SECRET,
      issuer: process.env.KC_ISSUER,
    }),
  ],
  callbacks: {
    async jwt({ token, account, profile }) {
      // Initial sign in
      if (account && profile) {
        token.accessToken = account.access_token
        token.refreshToken = account.refresh_token
        token.idToken = account.id_token
        token.uuid = profile.sub
      }
      return token
    },
    async session({ session, token }) {
      session.accessToken = token.accessToken
      session.refreshToken = token.refreshToken
      session.idToken = token.idToken
      session.uuid = token.uuid
      return session
    },
  },
})
