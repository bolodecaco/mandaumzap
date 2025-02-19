'use client'

import { signOut } from 'next-auth/react'

export function Content() {
  return <button onClick={() => signOut()}>Logout</button>
}
