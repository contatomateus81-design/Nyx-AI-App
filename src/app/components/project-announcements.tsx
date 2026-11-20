"use client";

import { useEffect, useState } from "react";

const announcements = [
  {
    title: "NYX AI",
    image: "/nyxads/1795044783908~2.webp",
    url: "https://nyx-ai-pearl.vercel.app",
    imageAlt: "Anúncio da NYX AI",
    variant: "ai",
  },
  {
    title: "NYX ClassCode",
    image: "/nyxads/1795045022798~2.webp",
    url: "https://nyx-classcode.vercel.app",
    imageAlt: "Anúncio da NYX ClassCode",
    variant: "classcode",
  },
];

export default function ProjectAnnouncements() {
  const [active, setActive] = useState(0);
  const [closed, setClosed] = useState(false);
  const [imageDimensions, setImageDimensions] = useState<{ width: number; height: number } | null>(null);
  const [viewport, setViewport] = useState({ width: 1024, height: 768 });

  useEffect(() => {
    const updateViewport = () => setViewport({ width: window.innerWidth, height: window.innerHeight });
    updateViewport();
    window.addEventListener("resize", updateViewport);
    return () => window.removeEventListener("resize", updateViewport);
  }, []);

  useEffect(() => {
    if (closed) return;

    const previousOverflow = document.body.style.overflow;
    document.body.style.overflow = "hidden";

    const onKeyDown = (event: KeyboardEvent) => {
      if (event.key === "Escape") {
        setActive((current) => {
          if (current === 0) return 1;
          setClosed(true);
          return current;
        });
      }
    };

    window.addEventListener("keydown", onKeyDown);
    return () => {
      document.body.style.overflow = previousOverflow;
      window.removeEventListener("keydown", onKeyDown);
    };
  }, [closed]);

  const dismiss = () => {
    if (active === 0) setActive(1);
    else setClosed(true);
  };

  if (closed) return null;

  const announcement = announcements[active];
  const imageWidth = imageDimensions
    ? Math.max(
        1,
        Math.min(
          imageDimensions.width,
          920,
          viewport.width - 32,
          (viewport.height - 44) * (imageDimensions.width / imageDimensions.height),
        ),
      )
    : undefined;

  return (
    <div className="announcement-overlay" role="presentation">
      <section
        className={`announcement-image-modal announcement-image-modal-${announcement.variant}`}
        role="dialog"
        aria-modal="true"
        aria-label={`Novidade: ${announcement.title}`}
        style={imageWidth ? { width: `${imageWidth}px` } : undefined}
      >
        <img
          className="announcement-image"
          src={announcement.image}
          alt={announcement.imageAlt}
          draggable={false}
          onLoad={(event) => {
            const image = event.currentTarget;
            setImageDimensions({ width: image.naturalWidth, height: image.naturalHeight });
          }}
        />

        <a
          className="announcement-image-visit"
          href={announcement.url}
          target="_blank"
          rel="noopener noreferrer"
          aria-label={`Visitar o site ${announcement.title}`}
        >
          VISITAR SITE
        </a>

        <button
          className="announcement-image-close"
          type="button"
          onClick={dismiss}
          aria-label={active === 0 ? "Fechar anúncio e ver o próximo" : "Fechar anúncio"}
        />
      </section>
    </div>
  );
}
