function CampingScene() {
    return (
        <div className="camping-scene">
            <svg viewBox="0 0 800 600" xmlns="http://www.w3.org/2000/svg">
                <!-- Sky gradient -->
                <defs>
                    <linearGradient id="skyGradient" x1="0%" y1="0%" x2="0%" y2="100%">
                        <stop offset="0%" stop-color="#00008B" />
                        <stop offset="30%" stop-color="#4B0082" />
                        <stop offset="60%" stop-color="#FF6347" />
                        <stop offset="80%" stop-color="#FFA500" />
                        <stop offset="100%" stop-color="#FFD700" />
                    </linearGradient>

                    <linearGradient id="groundGradient" x1="0%" y1="0%" x2="0%" y2="100%">
                        <stop offset="0%" stop-color="#228B22" />
                        <stop offset="100%" stop-color="#006400" />
                    </linearGradient>

                    <radialGradient id="sunGlow" cx="50%" cy="50%" r="50%" fx="50%" fy="50%">
                        <stop offset="0%" stop-color="#FFD700" stop-opacity="1" />
                        <stop offset="100%" stop-color="#FF8C00" stop-opacity="0" />
                    </radialGradient>
                </defs>

                <!-- Background -->
                <rect x="0" y="0" width="800" height="400" fill="url(#skyGradient)" />
                <rect x="0" y="400" width="800" height="200" fill="url(#groundGradient)" />

                <!-- Sun -->
                <circle cx="400" cy="350" r="80" fill="#FF4500" />
                <circle cx="400" cy="350" r="120" fill="url(#sunGlow)" />

                <!-- Mountains in background -->
                <polygon points="0,400 150,200 300,350 450,180 600,320 750,230 800,400" fill="#556B2F" />
                <polygon points="50,400 200,280 350,400" fill="#2F4F4F" />
                <polygon points="500,400 650,250 800,400" fill="#2F4F4F" />

                <!-- Trees -->
                <g transform="translate(120, 380)">
                    <rect x="-5" y="0" width="10" height="20" fill="#8B4513" />
                    <polygon points="-20,-15 20,-15 0,-50" fill="#228B22" />
                    <polygon points="-15,0 15,0 0,-30" fill="#228B22" />
                </g>

                <g transform="translate(650, 390)">
                    <rect x="-6" y="0" width="12" height="25" fill="#8B4513" />
                    <polygon points="-25,-20 25,-20 0,-60" fill="#006400" />
                    <polygon points="-20,0 20,0 0,-40" fill="#006400" />
                </g>

                <!-- Lake reflection -->
                <ellipse cx="400" cy="450" rx="150" ry="30" fill="#4682B4" opacity="0.7" />
                <path d="M250,450 Q400,465 550,450" stroke="#FFD700" stroke-width="1" fill="none" opacity="0.5" />

                <!-- Camp tent -->
                <polygon points="300,400 350,350 400,400" fill="#B22222" />
                <polygon points="400,400 350,350 450,350 500,400" fill="#8B0000" />
                <rect x="390" y="375" width="20" height="25" fill="#000000" />

                <!-- Campfire -->
                <circle cx="350" cy="430" r="10" fill="#8B4513" />
                <polygon points="345,430 350,415 355,430" fill="#FF4500" />
                <polygon points="340,425 350,415 360,425" fill="#FFA500" />

                <!-- Smoke from campfire -->
                <path d="M350,415 Q345,405 350,395 Q355,385 350,375" stroke="#DCDCDC" stroke-width="2" fill="none" opacity="0.5" />

                <!-- Stars in the sky -->
                <g>
                    <circle cx="100" cy="100" r="2" fill="white" />
                    <circle cx="200" cy="50" r="1.5" fill="white" />
                    <circle cx="300" cy="150" r="2" fill="white" />
                    <circle cx="500" cy="100" r="1.5" fill="white" />
                    <circle cx="600" cy="180" r="2" fill="white" />
                    <circle cx="700" cy="80" r="1.5" fill="white" />
                    <circle cx="750" cy="150" r="2" fill="white" />
                    <circle cx="150" cy="180" r="1.5" fill="white" />
                </g>
            </svg>
        </div>
    );
}

export default CampingScene;