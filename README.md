# About

**Bullet** is a minecraft server built from the ground up with the goal of being lightweight, fast, and easy to use.
> [!IMPORTANT]
> Bullet is currently in development and is not ready for production use

# Why Bullet?

The vanilla minecraft server that is written by Mojang, comes with a host of limitations that can make large scale or even medium-sized servers frustrating and expensive to manage.

### Biggest problem with vanilla servers
<ol>
    <li>Performance - Vanilla servers demand an absurd amount of RAM, even to support 10 players, an absolute minimum of 8GB of ram is needed to run smoothly.</li>
    <li>Single threaded - The core server is single threaded, meaning the server can't efficiently use multi core CPU's. This means server owners need to invest heavily into a CPU with high single core performance, and disregard the number of cores on the CPU.</li>
    <li>Limited Modularity - Extending vanilla functionality requires major workarounds, bukkit was created to solve this problem but had performance issues, spigot was created to solve bukkits problems, paper was created to solve spigots problems, and so on. This has created a fragmented ecosystem of plugins that are not always compatible with each other and brings unncessary complexity to server management and plugin development.</li>
</ol>

### Bullet's solution
<ol>
    <li>Performance - Bullet is designed to be lightweight and fast, with a focus on performance. Since bullet is built from the ground up, it can be infinity optimized for performance and memory usage.</li>
    <li>Multi threaded - Bullet is designed to be multi threaded, so it can efficiently distribute tasks across multiple CPU cores, drastically improving tick rate and overall performance.</li>
    <li>Familiar and easy to use - While Bullet provides low level control for developers, it also maintains a familiar environment for those transitioning from spigot or paper.</li>
    <li>NMS - Bullet doesn't rely on outdated mojang code, and instead does everything from scratch.</li>
</ol>

<img src="./img/comparision.png" alt="Comparison chart">

### Why not use minestom or another pre existing server?
<ol>
    <li>Steep learning curve - Switching to an entirely new framework requires rewriting plugins and understanding a new complicated API. Developers first coming from spigot or paper, shouldn't have to implement world generation or chunk loading from scratch.</li>
    <li>Lack of flexibility - Some alternative servers prioritize speed but sacrifice modularity and customization, limiting what developers can achieve</li>
    <li>Incompleteness - Many of the custom servers out there, don't fully implement vanilla mechanics in the way the official server does</li>
</ol>

# How to get started

Work in progress..

# License

Bullet is licensed under the Mozilla Public License 2.0, you can find the license [here](LICENSE)