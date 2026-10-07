# MentionPing

Never miss being called out in chat again. MentionPing listens to
incoming chat messages, and the moment your username appears in one
(case-insensitive), it plays a bright note-block ping and flashes a
yellow "You were mentioned in chat!" notice above the hotbar for
3 seconds.

## Features

- **Mention ping sound** — a soft note-block pling fires whenever your
  username shows up in chat
- **HUD flash** — yellow notice above the hotbar stays on screen for
  3 seconds so you notice it even mid-fight
- **Case-insensitive matching** — "Steve", "STEVE" and "steve" all
  trigger the ping
- **Zero config** — drop it in and play

## Install

1. Install [Fabric Loader](https://fabricmc.net/use/) 0.19.2+ for
   Minecraft 26.2
2. Install [Fabric API](https://modrinth.com/mod/fabric-api) for
   Minecraft 26.2
3. Drop `mentionping-1.0.0.jar` into your `mods` folder
4. Launch and play — no config needed

## Notes

- Client-side only. Works on any server (including vanilla).
- No mixins, no networking, nothing saved to disk.

## License

MIT — see [LICENSE](LICENSE).
